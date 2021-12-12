package ru.vsu.cs.proskuryakov.coffeestrike.app.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.vsu.cs.proskuryakov.coffeestrike.app.exceptions.NotFoundException;
import ru.vsu.cs.proskuryakov.coffeestrike.app.security.models.User;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Slf4j
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandlerImpl.class);

    private final UserRepository userRepository;
    private final MapperFacade mapperFacade;
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public AuthenticationSuccessHandlerImpl(UserRepository userRepository, MapperFacade mapperFacade) {
        this.userRepository = userRepository;
        this.mapperFacade = mapperFacade;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        onAuthenticationSuccess(request, response, authentication);
    }

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setStatus(HttpServletResponse.SC_OK);

        var object = authentication.getPrincipal();

        if (!(object instanceof UserDetails)) {
            logger.error("Expected UserDetails but got {}", object.getClass());
            throw new ServletException("Unexpected authorization class");
        }

        var userItem = userRepository.findUserItemByUsername(((UserDetails) object).getUsername());
        if(userItem.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        var user = mapperFacade.map(userItem.get(), User.class);
        user.setPassword(null);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                user, null, Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));

        response.setCharacterEncoding("UTF-8");

        OBJECT_MAPPER.writeValue(response.getWriter(), user);
        logger.info("Authentication successfully. {}", user);
    }

}
