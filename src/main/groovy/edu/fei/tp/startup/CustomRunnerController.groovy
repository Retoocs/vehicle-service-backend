package edu.fei.tp.startup

import com.netgrif.application.engine.startup.*

class CustomRunnerController extends RunnerController {

    private List order = [
            ElasticsearchRunner,
            MongoDbRunner,
            StorageRunner,
            RuleEngineRunner,
            DefaultRoleRunner,
            AnonymousRoleRunner,
            AuthorityRunner,
            SystemUserRunner,
            FunctionsCacheRunner,
            FilterRunner,
            GroupRunner,
            DefaultFiltersRunner,
            SuperCreator,
            FlushSessionsRunner,
            MailRunner,
            PostalCodeImporter,
            DemoRunner,
            QuartzSchedulerRunner,
            PdfRunner,
            FinisherRunnerSuperCreator,
            // ADDITIONAL CUSTOM RUNNERS - keep order
            NetImportRunner,
            UserRunner,
            CaseImportRunner,
            CsvImportRunner,
            // END OF ADDITIONAL CUSTOM RUNNERS
            FinisherRunner,
            VSFinisherRunner,
    ]

    @Override
    protected List getOrderList() {
        return order
    }

}
