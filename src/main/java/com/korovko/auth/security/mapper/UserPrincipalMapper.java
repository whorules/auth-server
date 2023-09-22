package com.korovko.auth.security.mapper;

import com.korovko.auth.entity.User;
import com.korovko.auth.security.dto.CurrentUserDto;
import com.korovko.auth.security.dto.UserPrincipal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserPrincipalMapper {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "authorities", source = "entity", qualifiedByName = "authoritiesMapping")
    UserPrincipal entityToDto(User entity);

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "username", source = "username")
    CurrentUserDto toCurrentUser(UserPrincipal principal);

    @Named("authoritiesMapping")
    default Set<SimpleGrantedAuthority> authoritiesMapping(User user) {
        return Set.of(new SimpleGrantedAuthority(user.getRole().name()));
    }
}
