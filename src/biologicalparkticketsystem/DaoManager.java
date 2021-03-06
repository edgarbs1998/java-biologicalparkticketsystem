package biologicalparkticketsystem;

import biologicalparkticketsystem.model.course.MapManager;
import biologicalparkticketsystem.model.document.IInvoiceDAO;
import biologicalparkticketsystem.model.statistic.IStatisticsDAO;
import biologicalparkticketsystem.model.document.ITicketDAO;
import biologicalparkticketsystem.model.document.InvoiceDAOSerialization;
import biologicalparkticketsystem.model.document.InvoiceDAOSqlLite;
import biologicalparkticketsystem.model.statistic.StatisticsDAOSerialization;
import biologicalparkticketsystem.model.statistic.StatisticsDAOSqlLite;
import biologicalparkticketsystem.model.document.TicketDAOSerialization;
import biologicalparkticketsystem.model.document.TicketDAOSqlLite;

/**
 * Class to manage the presistance of data in tickets, invoices and statistics
 */
public class DaoManager {
    
    private static DaoManager instance = new DaoManager();
    
    private ITicketDAO ticketDao;
    private IInvoiceDAO invoiceDao;
    private IStatisticsDAO statisticsDao;
    
    private DaoManager() { }
    
    /**
     * Method to get the instance of singleton class DaoManager
     * @return instance of singleton class DaoManager
     */
    public static DaoManager getInstance() {
        return instance;
    }
    
    /**
     * Method to initialize the data trought serialization or sqllite
     * @param config config manager instance
     * @param mapManager map manager instance
     */
    public void init(ConfigManager config, MapManager mapManager) {
        switch (config.getProperties().getProperty("persistence.type")) {
            case "serialization":
                this.ticketDao = new TicketDAOSerialization(config.getProperties().getProperty("persistence.erialization.folder"));
                this.invoiceDao = new InvoiceDAOSerialization(config.getProperties().getProperty("persistence.erialization.folder"));
                this.statisticsDao = new StatisticsDAOSerialization(config.getProperties().getProperty("persistence.erialization.folder"), mapManager);
                break;
            case "sqllite":
                this.ticketDao = new TicketDAOSqlLite(config.getProperties().getProperty("persistence.sqllite.file"));
                this.invoiceDao = new InvoiceDAOSqlLite(config.getProperties().getProperty("persistence.sqllite.file"));
                this.statisticsDao = new StatisticsDAOSqlLite(config.getProperties().getProperty("persistence.sqllite.file"), mapManager);
                break;
            default:
                throw new IllegalArgumentException("ticket dao type does not exists");
        }
    }
    
    /**
     * Method to get the ticket dao instance
     * @return data access object of ticket
     */
    public ITicketDAO getTicketDao() {
        return this.ticketDao;
    }
    
    /**
     * Method to get the invoice dao instance
     * @return data access object of invoice
     */
    public IInvoiceDAO getInvoiceDao() {
        return this.invoiceDao;
    }
    
    /**
     * Method to get the statistics dao instance
     * @return data access object of statistics
     */
    public IStatisticsDAO getStatisticsDao() {
        return this.statisticsDao;
    }
    
}
