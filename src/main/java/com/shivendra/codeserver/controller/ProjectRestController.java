package com.shivendra.codeserver.controller;


import com.shivendra.codeserver.service.ProjectService;
import com.shivendra.codeserver.entity.Project;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(ProjectRestController.ENDPOINT)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, tags = "Project")
public class ProjectRestController {

	public static final String ENDPOINT = "/api/v2/projects";
	public static final String ENDPOINT_ID = "/{id}";
	public static final String PATH_VARIABLE_ID = "id";

	private static final String API_PARAM_ID = "ID";

	@Autowired
	private ProjectService projectService;

	@ApiOperation("Get a Project")
	@GetMapping(ENDPOINT_ID)
	public Project getProject(
			@ApiParam(name = API_PARAM_ID, required = true)
			@PathVariable(PATH_VARIABLE_ID) final long projectId
	) {
		return projectService.getProject(projectId);
	}


	@ApiOperation("Create a Project")
	@PostMapping
	public ResponseEntity createProject(@Valid @RequestBody Project project, UriComponentsBuilder ucBuilder) {
		Project ctreatedProject = projectService.createProject(project);
		HttpHeaders headers = new HttpHeaders();
		StringBuilder builder = new StringBuilder();
		builder.append(ENDPOINT);
		builder.append(ENDPOINT_ID);
		headers.setLocation(ucBuilder.path(builder.toString()).buildAndExpand(ctreatedProject.getId()).toUri());
		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@ApiOperation("Update a Project")
	@PatchMapping(ENDPOINT_ID)
	public ResponseEntity patchProject(@ApiParam(name = API_PARAM_ID, required = true)
									   @PathVariable(PATH_VARIABLE_ID) final long projectId,
									   @Valid @RequestBody Map patch, UriComponentsBuilder ucBuilder) {

			return ResponseEntity.ok(projectService.updateProject(patch,projectId));

	}

}
