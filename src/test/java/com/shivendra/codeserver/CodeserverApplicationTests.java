package com.shivendra.codeserver;

import com.shivendra.codeserver.application.CodeserverApplication;
import com.shivendra.codeserver.entity.Project;
import com.shivendra.codeserver.entity.SdlcSystem;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CodeserverApplication.class)
@AutoConfigureMockMvc
class CodeserverApplicationTests {

	public static final String ENDPOINT = "/api/v2/projects";

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
		Assert.assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();

	}

	@Test
	public void should_create_valid_project_and_return_created_status() throws Exception {
		Project project = new Project();
		SdlcSystem system = new SdlcSystem();
		project.setExternalId("EXTERNALID");
		project.setName("Name");
		system.setId(1);
		project.setSdlcSystem(system);

		mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(project)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", Matchers.containsString(ENDPOINT)))
				.andExpect(content().string(""));

	}

	@Test
	public void should_not_create_project_and_return_bad_request() throws Exception {
		Project project = new Project();
		SdlcSystem system = new SdlcSystem();
		system.setId(1);
		project.setSdlcSystem(system);

		mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(project)))
				.andExpect(status().isBadRequest());

	}

	@Test
	public void should_not_create_project_and_return_conflict() throws Exception {
		Project project = new Project();
		project.setExternalId("SAMPLEPROJECT");
		SdlcSystem system = new SdlcSystem();
		system.setId(1);
		project.setSdlcSystem(system);

		mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(project)))
				.andExpect(status().isConflict());

	}

	@Test
	public void should_not_create_project_and_return_not_found() throws Exception {
		Project project = new Project();
		project.setExternalId("SAMPLEPROJECT");
		SdlcSystem system = new SdlcSystem();
		system.setId(25);
		project.setSdlcSystem(system);

		mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(project)))
				.andExpect(status().isNotFound());

	}

	@Test
	public void should_update_valid_project_and_return_success_status() throws Exception {
		Project project = new Project();
		SdlcSystem system = new SdlcSystem();
		project.setExternalId("EXTERNALEDITED");
		project.setName("Name-Edited");
		system.setId(1);
		project.setSdlcSystem(system);

		mockMvc.perform(patch(ENDPOINT+"/5")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(project)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.externalId", Is.is("EXTERNALEDITED")))
				.andExpect(jsonPath("$.name", Is.is("Name-Edited")))
				.andExpect(jsonPath("$.sdlcSystem.id", Is.is(1)));

	}

	@Test
	public void should_update_valid_project_without_sdlc_and_return_success_status() throws Exception {
		Project project = new Project();
		project.setExternalId("EXTERNALEDITED");

		mockMvc.perform(patch(ENDPOINT+"/6")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(project)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.externalId", Is.is("EXTERNALEDITED")));


	}

	@Test
	public void should_update_valid_project_with_null_name_and_return_success_status() throws Exception {
		Project project = new Project();
		project.setName(null);

		mockMvc.perform(patch(ENDPOINT+"/5")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(project)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.name", Matchers.nullValue()));


	}

	@Test
	public void should_not_update_project_and_return_bad_request_due_to_invalid_system() throws Exception {

		Map innerMap = new HashMap();
		innerMap.put("id","whatever");
		Map map = new HashMap();
		map.put("sdlcSystem",innerMap);

		mockMvc.perform(patch(ENDPOINT+"/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(innerMap)))
				.andExpect(status().isBadRequest());


	}

	@Test
	public void should_not_update_project_and_return_not_found() throws Exception {
		Project project = new Project();
		SdlcSystem system = new SdlcSystem();
		system.setId(12345);
		project.setSdlcSystem(system);

		mockMvc.perform(patch(ENDPOINT+"/6")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(project)))
				.andExpect(status().isNotFound());


	}

	@Test
	public void should_not_update_project_and_return_conflict_due_to_system() throws Exception {
		Map innerMap = new HashMap();
		innerMap.put("id",2);
		Map map = new HashMap();
		map.put("sdlcSystem",innerMap);


		mockMvc.perform(patch(ENDPOINT+"/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(map)))
				.andExpect(status().isConflict());


	}

	@Test
	public void should_not_update_project_and_return_conflict_due_to_externalId() throws Exception {
		Project project = new Project();
		project.setExternalId("PROJECTX");


		mockMvc.perform(patch(ENDPOINT+"/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(project)))
				.andExpect(status().isConflict());


	}

	@Test
	public void should_not_update_project_and_return_conflict_due_to_externalId_and_system() throws Exception {
		Project project = new Project();
		project.setExternalId("PROJECTX");
		SdlcSystem system = new SdlcSystem();
		system.setId(2);



		mockMvc.perform(patch(ENDPOINT+"/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(project)))
				.andExpect(status().isConflict());


	}

	@Test
	public void should_not_update_project_and_return_bad_request_due_to_illeagal_id() throws Exception {

		mockMvc.perform(patch(ENDPOINT+"/whatever")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(new HashMap<>())))
				.andExpect(status().isBadRequest());


	}

	@Test
	public void should_not_update_project_and_return_not_found_due_to_invalid_id() throws Exception {

		mockMvc.perform(patch(ENDPOINT+"/12345")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json(new HashMap<>())))
				.andExpect(status().isNotFound());


	}

}




