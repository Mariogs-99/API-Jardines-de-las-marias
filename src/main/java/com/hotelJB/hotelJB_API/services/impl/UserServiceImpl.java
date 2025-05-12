    package com.hotelJB.hotelJB_API.services.impl;

    import com.hotelJB.hotelJB_API.models.dtos.LoginDTO;
    import com.hotelJB.hotelJB_API.models.dtos.SingupDTO;
    import com.hotelJB.hotelJB_API.models.entities.Token;
    import com.hotelJB.hotelJB_API.models.entities.User_;
    import com.hotelJB.hotelJB_API.repositories.TokenRepository;
    import com.hotelJB.hotelJB_API.repositories.UserRepository;
    import com.hotelJB.hotelJB_API.security.JWTTools;
    import com.hotelJB.hotelJB_API.services.UserService;
    import jakarta.transaction.Transactional;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

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

        @Override
        public void login(LoginDTO data) throws Exception {
            User_ user = userRepository.findByUsername(data.getUsername());

            if (!comparePass(data.getPassword(), user.getPassword())) {
                throw new Exception("Invalid credentials");
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

            return userRepository.findByUsername(username);    }
    }
