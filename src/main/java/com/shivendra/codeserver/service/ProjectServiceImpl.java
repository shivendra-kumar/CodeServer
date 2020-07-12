package com.shivendra.codeserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shivendra.codeserver.entity.Project;
import com.shivendra.codeserver.entity.SdlcSystem;
import com.shivendra.codeserver.exception.NotFoundException;
import com.shivendra.codeserver.exception.ResourceAlreadyExistsException;
import com.shivendra.codeserver.repository.ProjectRepository;
import com.shivendra.codeserver.repository.SdlcSystemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final SdlcSystemRepository sdlcSystemRepository;

    private static ObjectMapper mapper = new ObjectMapper();

    private final ModelMapper modelMapper;

    @Override
    public Project getProject(long id) {
        return projectRepository.findById(id).orElseThrow(() -> new NotFoundException(Project.class, id));
    }

    @Override
    public Project createProject(Project project) {
        if(project.getSdlcSystem()==null){
            throw new IllegalArgumentException("SdlcSystem missing from request.");
        }
        if(project.getExternalId()==null || project.getExternalId().isEmpty()){
            throw new IllegalArgumentException("ExternalId missing from request.");
        }
        Optional<SdlcSystem> optional = sdlcSystemRepository.findById(project.getSdlcSystem().getId());
        if (optional.isPresent()) {
            return create(project, optional.get());
        } else {
            throw new NotFoundException(SdlcSystem.class, project.getSdlcSystem().getId());
        }

    }

    @Override
    public Project updateProject(Map patch, long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException(Project.class, id));
        applyPatchToProject(mapper.convertValue(patch,Project.class), project,patch);
        Project updatedProject = null;
        try {
            updatedProject = projectRepository.save(project);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistsException();

        }
        return updatedProject;
    }

    private void applyPatchToProject(Project patch, Project project,Map map) {
        if (patch.getExternalId() != null) {
            project.setExternalId(patch.getExternalId());
        }
        if (map.keySet().contains("name") || patch.getName() != null) {
            project.setName(patch.getName());
        }
        SdlcSystem sdlcPatch = patch.getSdlcSystem();
        if (sdlcPatch != null) {
            Optional<SdlcSystem> optional = sdlcSystemRepository.findById(sdlcPatch.getId());
            if (optional.isPresent()) {
                project.setSdlcSystem(optional.get());
            } else {
                throw new NotFoundException(SdlcSystem.class, project.getSdlcSystem().getId());
            }

        }
    }


    private Project create(Project project, SdlcSystem system) {
        project.setSdlcSystem(system);
        Project newProject = new Project();
        modelMapper.map(project, newProject);
        Project createdProject = null;
        try {
            createdProject = projectRepository.save(newProject);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistsException();

        }
        return createdProject;
    }


}
