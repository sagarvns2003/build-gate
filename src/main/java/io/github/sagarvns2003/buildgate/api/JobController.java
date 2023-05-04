package io.github.sagarvns2003.buildgate.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.sagarvns2003.buildgate.manager.JobManager;
import io.github.sagarvns2003.buildgate.model.JobRequest;

@RestController
@RequestMapping(path = "/v1/api/job")
public class JobController {

	private static final Logger logger = LoggerFactory.getLogger(JobController.class);

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private JobManager jobManager;

	@PostMapping(value = "/enqueue", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> enqueueJob(@RequestBody JobRequest jobRequest) {
		logger.info("Submitting job...");
		this.jobManager.enqueueJob(jobRequest);
		return new ResponseEntity("Added", HttpStatus.OK);
	}

	@GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getJobInfo() {
		logger.info("Getting all job info ");
		var info = this.jobManager.getAllJobInformation();
		return new ResponseEntity(info, HttpStatus.OK);
	}

	@GetMapping(value = "/info/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getJobInfo(@PathVariable(name = "id", required = true) String jobId) {

		logger.info("Getting job info for the jobId: {}", jobId);
		var info = this.jobManager.getJobInformation(jobId);
		if (null == info) {
			return new ResponseEntity("{}", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(info, HttpStatus.OK);
	}

	/// ----------------------------

	@PostMapping("/send")
	public ResponseEntity<Void> sendMessage(@RequestBody String textMessageDTO) {
		logger.info("Got message: {}", textMessageDTO);
		template.convertAndSend("/topic/message", textMessageDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@MessageMapping("/sendMessage")
	public void receiveMessage(@Payload String textMessageDTO) {
		logger.info("Got message: {}", textMessageDTO);
		template.convertAndSend("/topic/message", textMessageDTO);
	}

	@SendTo("/topic/message")
	public String broadcastMessage(@Payload String textMessageDTO) {
		logger.info("broadcastMessage: {}", textMessageDTO);
		return textMessageDTO;
	}

}
