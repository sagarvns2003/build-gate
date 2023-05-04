package io.github.sagarvns2003.buildgate.api;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
@RequestMapping(path = "/build/v1/api")
public class BuildServerControler {

	private static final Logger logger = LoggerFactory.getLogger(BuildServerControler.class);

	private ExecutorService nonBlockingService = Executors.newCachedThreadPool();

	@GetMapping(value="/info", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<StreamingResponseBody> handleRbe() {
		StreamingResponseBody stream = out -> {
			String msg = "/srb" + " @ " + new Date();
			out.write(msg.getBytes());
		};
		return new ResponseEntity(stream, HttpStatus.OK);
	}

	@GetMapping(value = "/entry-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter findEventsByDateRangeSSE() {
		SseEmitter emitter = new SseEmitter();
		nonBlockingService.execute(() -> {
			try {
				for (int i = 0; true; i++) {
					List<String> entries = List.of("ABC", "PQR");

					SseEventBuilder event = SseEmitter.event().id(String.valueOf(i)).name("view calender entry")
							.data(entries, MediaType.APPLICATION_JSON);

					emitter.send(event);
					Thread.sleep(2000);
				}
			} catch (Exception ex) {
				emitter.completeWithError(ex);
			}
		});
		return emitter;
	}

}
