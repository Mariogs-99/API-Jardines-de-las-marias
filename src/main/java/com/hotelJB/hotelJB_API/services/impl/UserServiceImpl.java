    package com.hotelJB.hotelJB_API.services.impl;

    import com.hotelJB.hotelJB_API.models.dtos.LoginDTO;
    import com.hotelJB.hotelJB_API.models.dtos.SingupDTO;
    import com.hotelJB.hotelJB_API.models.entities.Role;
    import com.hotelJB.hotelJB_API.models.entities.Token;
    import com.hotelJB.hotelJB_API.models.entities.User_;
    import com.hotelJB.hotelJB_API.models.responses.LoginResponse;
    import com.hotelJB.hotelJB_API.repositories.RoleRepository;
    import com.hotelJB.hotelJB_API.repositories.TokenRepository;
    import com.hotelJB.hotelJB_API.repositories.UserRepository;
    import com.hotelJB.hotelJB_API.security.JWTTools;
    import com.hotelJB.hotelJB_API.services.UserService;
    import jakarta.transaction.Transactional;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import com.hotelJB.hotelJB_API.models.dtos.UserDTO;
    import com.hotelJB.hotelJB_API.models.responses.UserResponse;


    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class UserServiceImpl implements UserService {

        @Autowired
        public PasswordEncoder passwordEncoder;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private JWTTools jwtTools;

        @Autowired
        private TokenRepository tokenRepository;

        @Autowired
        private RoleRepository roleRepository;


        @Override
        @Transactional(rollbackOn = Exception.class)
        public void save(SingupDTO data) throws Exception {
            try {
                User_ user = new User_(data.getUsername(), passwordEncoder.encode(data.getPassword()));

                userRepository.save(user);

            } catch (Exception e) {
                throw new Exception("Error save user");
            }
        }

        //*Metodo implementado para validad si el usuario existe en la base de datos
        @Override
        public void login(LoginDTO data) throws Exception {
            User_ user = userRepository.findByUsername(data.getUsername());

            if (user == null) {
                throw new Exception("Credenciales inválidas");
            }

            //!Validar si el usuario está inactivo
            if (user.getActive() == null || !user.getActive()) {
                throw new Exception("Tu cuenta está inactiva. Contacta al administrador.");
            }

            //?Validar contraseña
            if (!comparePass(data.getPassword(), user.getPassword())) {
                throw new Exception("Credenciales inválidas");
            }
        }


        @Override
        public User_ findByUsername(String username) {
            return userRepository.findByUsername(username);
        }

        @Override
        public List<User_> findAll() {
            return userRepository.findAll();
        }

        @Override
        @Transactional(rollbackOn = Exception.class)
        public Token registerToken(User_ user) throws Exception {
            cleanTokens(user);

            String tokenString = jwtTools.generateToken(user);
            Token token = new Token(tokenString, user,true);

            tokenRepository.save(token);

            return token;
        }

        @Override
        public Boolean isTokenValid(User_ user, String token) {
            try {
                cleanTokens(user);
                List<Token> tokens = tokenRepository.findByUserAndActive(user, true);

                tokens.stream().filter(tk -> tk.getToken().equals(token)).findAny().orElseThrow(() -> new Exception());

                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        @Transactional(rollbackOn = Exception.class)
        public void cleanTokens(User_ user) throws Exception {
            List<Token> tokens = tokenRepository.findByUserAndActive(user, true);

            for (Token token : tokens) {
                try {
                    if (!jwtTools.verifyToken(token.getToken())) {
                        token.setActive(false);
                        tokenRepository.save(token);
                    }
                } catch (Exception e) {
                    // Manejar la excepción según sea necesario
                    throw new RuntimeException("Error al desactivar el token: " + token.getIdToken(), e);
                }
            }
        }

        @Override
        public User_ getUserFromToken(String info) {
            List<Token> token = tokenRepository.findAll().stream()
                    .filter(t -> t.getToken().matches(info) && t.getActive().equals(true)).collect(Collectors.toList());
            User_ user = token.get(0).getUser();

            return user;
        }

        @Override
        public Boolean comparePass(String toCompare, String current) {
            return passwordEncoder.matches(toCompare, current);
        }

        @Override
        public void toggleToken(User_ user) {
            List<Token> tokens = tokenRepository.findByUserAndActive(user, true);

            if (!tokens.isEmpty()) {
                Token token = tokens.get(0);
                token.setActive(false);
                tokenRepository.save(token);
            }
        }

        @Override
        public User_ findUserAuthenticated() {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            return userRepository.findByUsername(username);
        }

        //!Nuevos metodos para la creacion de los usuarios

        @Override
        public List<UserResponse> getAllUsers() {
            return userRepository.findAll().stream().map(user -> {
                UserResponse res = new UserResponse();
                res.setUserId(user.getUserId());
                res.setUsername(user.getUsername());
                res.setFirstname(user.getFirstname());
                res.setLastname(user.getLastname());
                res.setEmail(user.getEmail());
                res.setPhone(user.getPhone());
                res.setActive(user.getActive());
                res.setRole(user.getRole() != null ? user.getRole().getName() : null); // Asignar rol aquí
                return res;
            }).collect(Collectors.toList());
        }


        @Override
        public UserResponse getUserById(int id) throws Exception {
            User_ user = userRepository.findById(id)
                    .orElseThrow(() -> new Exception("Usuario no encontrado"));
            UserResponse res = new UserResponse();
            res.setUserId(user.getUserId());
            res.setUsername(user.getUsername());
            res.setFirstname(user.getFirstname());
            res.setLastname(user.getLastname());
            res.setEmail(user.getEmail());
            res.setPhone(user.getPhone());
            res.setActive(user.getActive());
            res.setRole(user.getRole() != null ? user.getRole().getName() : null);
            return res;
        }


        @Override
        @Transactional
        public void createUser(UserDTO dto) throws Exception {
            if (userRepository.findByUsername(dto.getUsername()) != null) {
                throw new Exception("El nombre de usuario ya existe");
            }

            User_ user = new User_();
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setFirstname(dto.getFirstname());
            user.setLastname(dto.getLastname());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setActive(dto.getActive() != null ? dto.getActive() : true);

            if (dto.getRole() != null) {
                Role role = roleRepository.findByName(dto.getRole())
                        .orElseThrow(() -> new Exception("Rol no encontrado"));
                user.setRole(role);
            } else {
                // Opcional: asignar un rol por defecto
                Role defaultRole = roleRepository.findByName("USER")
                        .orElseThrow(() -> new Exception("Rol USER no encontrado"));
                user.setRole(defaultRole);
            }

            userRepository.save(user);
        }


        @Override
        @Transactional
        public void updateUser(int id, UserDTO dto) throws Exception {
            User_ user = userRepository.findById(id)
                    .orElseThrow(() -> new Exception("Usuario no encontrado"));

            // Verificar username
            User_ existing = userRepository.findByUsername(dto.getUsername());
            if (existing != null && existing.getUserId() != id) {
                throw new Exception("El nombre de usuario ya está en uso");
            }

            user.setUsername(dto.getUsername());
            user.setFirstname(dto.getFirstname());
            user.setLastname(dto.getLastname());
            user.setEmail(dto.getEmail());
            user.setPhone(dto.getPhone());
            user.setActive(dto.getActive());

            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            if (dto.getRole() != null) {
                Role role = roleRepository.findByName(dto.getRole())
                        .orElseThrow(() -> new Exception("Rol no encontrado"));
                user.setRole(role);
            }

            userRepository.save(user);
        }



        @Override
        @Transactional
        public void deleteUser(int id) throws Exception {
            if (!userRepository.existsById(id)) {
                throw new Exception("El usuario no existe");
            }
            userRepository.deleteById(id);
        }

        @Override
        public LoginResponse loginWithToken(LoginDTO data) throws Exception {
            User_ user = userRepository.findByUsername(data.getUsername());

            if (user == null || !comparePass(data.getPassword(), user.getPassword())) {
                throw new Exception("Credenciales inválidas");
            }

            if (user.getActive() == null || !user.getActive()) {
                throw new Exception("Tu cuenta está inactiva. Contacta al administrador.");
            }

            // 🔐 Generar y registrar token
            Token token = registerToken(user);

            // 🎭 Obtener nombre del rol como lista de un solo string
            String role = user.getRole() != null ? user.getRole().getName() : null;

            return new LoginResponse(token.getToken(), role);
        }



    }
