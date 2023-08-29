package co.com.bancolombia.restclient;

import co.com.bancolombia.logtechnicalvnt.log.LoggerAdapter;
import co.com.bancolombia.model.parameters.Parameters;
import co.com.bancolombia.parameters.ParametersUseCase;
import co.com.bancolombia.persistencedocument.PersistenceQueueUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

import static co.com.bancolombia.commonsvnt.usecase.util.constants.Constants.PRODUCT_VTN;
import static co.com.bancolombia.util.constants.Constants.PARENT_DOC_PERSISTENCE;

@Component
public class ScheduledTasks implements SchedulingConfigurer {

    @Autowired
    private PersistenceQueueUseCase persistenceQueueUseCase;

    @Autowired
    private ParametersUseCase parametersUseCase;

    LoggerAdapter adapter = new LoggerAdapter(PRODUCT_VTN, "Scheduled_tasks", ScheduledTasks.class.getName());

    public String getCronExpr() {
        String timeOut = "0 0 21 * * *";
        Optional<Parameters> timeOutParameter = parametersUseCase.findByNameAndParent("DOCPERSISTENCE_CRON_EXPR",
                PARENT_DOC_PERSISTENCE);
        if (timeOutParameter.isPresent()) {
            timeOut = String.valueOf(timeOutParameter.get().getCode());
        }
        return timeOut;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                adapter.info("Task in progress...");
                persistenceQueueUseCase.retrieveMessages();
            }
        }, triggerContext -> {
            String cron = getCronExpr();
            adapter.info("cron " + cron);
            CronTrigger trigger = new CronTrigger(cron);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
