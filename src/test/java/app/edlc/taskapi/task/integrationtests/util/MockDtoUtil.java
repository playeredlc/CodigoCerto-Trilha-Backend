package app.edlc.taskapi.task.integrationtests.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.edlc.taskapi.task.integrationtests.data.TaskDto;

public class MockDtoUtil {
	public static Date mockedDate;
	
	public static void setMockDate() throws Exception {
		mockedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2000-01-01 00:00:00");
	}
	
	public static void mockTaskAttributes(TaskDto task) throws Exception {
		task.setTitle("Title Test");
		task.setDescription("Description Test");
		task.setPriority("medium");
		task.setStatus("pending");
		
		if(mockedDate == null)
			setMockDate();		
		task.setDeadline(mockedDate);
	}	
	public static void updateTaskAttributes(TaskDto task) {
		task.setTitle("Updated Title Test");
		task.setDescription("Updated Description Test");
		task.setPriority("high");
		task.setStatus("completed");
		task.setDeadline(null);
	}
}
