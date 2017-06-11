import org.apache.log4j.PropertyConfigurator;
import org.quartz.SchedulerException;

import java.io.IOException;

/**
 * Created by corpa on 5/11/17.
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, SchedulerException {

        PropertyConfigurator.configure("log4j.config");

        WatcherFrame.launch();

//        SchedulerFactory sf = new StdSchedulerFactory();
//        Scheduler sched = sf.getScheduler();
//
//        JobDetail getPriceJob = JobBuilder.newJob(RESTfulUtils.class).build();
//        Trigger t = newTrigger()
//                .withIdentity("trigger", "group1")
//                .startNow()
//                .withSchedule(simpleSchedule().withIntervalInMilliseconds(500).repeatForever())
//                .build();
//        sched.scheduleJob(getPriceJob, t);
//
//        sched.start();
//
//        sched.shutdown(true);

    }
}