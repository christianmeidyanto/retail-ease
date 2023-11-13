package com.retailease.service;

import com.retailease.entity.Role;
import com.retailease.entity.constant.ERole;

public interface RoleService {
    Role getOrsave(ERole erole);
}
