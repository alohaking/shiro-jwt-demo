package org.kevin.admin.service;

import org.kevin.admin.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    public String generateJwtToken(String userName);
    public UserDto getJwtTokenInfo(String userName);
    public void deleteLoginInfo(String userName);
    public UserDto getUserInfo(String userName);
    public List<String> getUserRoles(Long userId);
    public Set<String> getUserPermissions(Long roleId);
}
