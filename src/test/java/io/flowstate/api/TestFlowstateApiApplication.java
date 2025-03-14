package io.flowstate.api;

import org.springframework.boot.SpringApplication;

public class TestFlowstateApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(FlowstateApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
