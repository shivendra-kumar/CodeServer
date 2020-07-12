
package com.shivendra.codeserver.controller;

import com.shivendra.codeserver.entity.Project;
import com.shivendra.codeserver.repository.ProjectRepository;
import com.shivendra.codeserver.repository.SdlcSystemRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(produces = MediaType.APPLICATION_JSON_VALUE, tags = "SDLC System")
public class SdlcSystemRestController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SdlcSystemRepository sdlcSystemRepository;

    @ApiOperation("Get a Project")
    @GetMapping("/api/v1/sdlc-systems/{systemId}/projects/{id}")
    public ResponseEntity<Project> getProject(@PathVariable(value = "systemId") Long systemId,
                                              @PathVariable(value = "id") Long projectId) {
        return projectRepository.findBySdlcSystemIdAndId(systemId, projectId)
                .map(project -> ResponseEntity.ok().body(project))
                .orElseGet(() -> ResponseEntity.ok().build());
    }

    @ApiOperation("Create a Project")
    @PostMapping("/api/v1/sdlc-systems/{systemId}/projects")
    public Project createProject(@PathVariable(value = "systemId") Long systemId, @Valid @RequestBody Project project) {
        sdlcSystemRepository.findById(systemId).ifPresent(project::setSdlcSystem);
        return projectRepository.save(project);
    }

    @ApiOperation("Update a Project")
    @PutMapping("/api/v1/sdlc-systems/{systemId}/projects/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable(value = "systemId") Long systemId,
                                                 @PathVariable(value = "id") Long projectId,
                                                 @Valid @RequestBody Project projectDetails) {
        return projectRepository.findBySdlcSystemIdAndId(systemId, projectId)
                .map(project -> {
                    project.setExternalId(projectDetails.getExternalId());
                    project.setName(projectDetails.getName());
                    project.setSdlcSystem(projectDetails.getSdlcSystem());
                    final Project updatedProject = projectRepository.save(project);
                    return ResponseEntity.ok(updatedProject);
                })
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}

