package app.edlc.taskapi.user;

import org.springframework.data.jpa.repository.JpaRepository;

import app.edlc.taskapi.user.data.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long>{}
