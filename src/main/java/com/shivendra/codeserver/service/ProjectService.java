package com.shivendra.codeserver.service;


import com.shivendra.codeserver.entity.Project;

import java.util.Map;

public interface ProjectService {

    Project getProject(long id);

    Project createProject(Project project);

    Project updateProject(Map patch, long id);
}
