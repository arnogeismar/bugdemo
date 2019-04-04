package com.test.test.testiets.demo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@SpringBootApplication
public class DemoApplication extends JFrame {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public DemoApplication() {

		initUI();
	}

	private void initUI() {

		JButton quitButton = new JButton("QUEUE");

		quitButton.addActionListener((ActionEvent event) -> {
			PayloadWrapper payloadWrapper = new PayloadWrapper();
			rabbitTemplate.convertAndSend("amq.direct", "",payloadWrapper);
		});

		createLayout(quitButton);

		setTitle("PAYLOAD SENDER");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void createLayout(JComponent... arg) {

		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);

		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addComponent(arg[0])
		);

		gl.setVerticalGroup(gl.createSequentialGroup()
				.addComponent(arg[0])
		);
	}

	public static void main(String[] args) {

		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(DemoApplication.class)
				.headless(false).run(args);

		EventQueue.invokeLater(() -> {
			DemoApplication ex = ctx.getBean(DemoApplication.class);
			ex.setVisible(true);
		});
	}


}
