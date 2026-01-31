package sk.upjs.paz.dao;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import sk.upjs.paz.dao.psdao.*;

public enum Factory {
    INSTANCE;

    private volatile JdbcOperations jdbc;

    private volatile UserDao UserDao;
    private volatile DistrictDao districtDao;
    private volatile TermDao termDao;
    private volatile StatusDao statusDao;
    private volatile MessageDao messageDao;
    private volatile ScheduleDao scheduleDao;
    private volatile StatusHistoryDao statusHistoryDao;

    private final Object lock = new Object();

    private JdbcOperations createJdbc() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setURL(System.getProperty(
                "DB_JDBC",
                "jdbc:postgresql://localhost:5433/cn"
        ));
        ds.setUser(System.getProperty("DB_USER", "casnast"));
        ds.setPassword(System.getProperty("DB_PASSWORD", "casnast"));
        return new JdbcTemplate(ds);
    }

    public JdbcOperations getJdbcOperations() {
        JdbcOperations local = jdbc;
        if (local == null) {
            synchronized (lock) {
                local = jdbc;
                if (local == null) {
                    local = createJdbc();
                    jdbc = local;
                }
            }
        }
        return local;
    }

    public UserDao getUserDao() {
        if (UserDao == null) {
            synchronized (lock) {
                if (UserDao == null) {
                    UserDao = new PostgresUserDao(getJdbcOperations());
                }
            }
        }
        return UserDao;
    }

    public DistrictDao getDistrictDao() {
        if (districtDao == null) {
            synchronized (lock) {
                if (districtDao == null) {
                    districtDao = new PostgresDistrictDao(getJdbcOperations());
                }
            }
        }
        return districtDao;
    }

    public TermDao getTermDao() {
        if (termDao == null) {
            synchronized (lock) {
                if (termDao == null) {
                    termDao = new PostgresTermDao(getJdbcOperations());
                }
            }
        }
        return termDao;
    }

    public StatusDao getStatusDao() {
        if (statusDao == null) {
            synchronized (lock) {
                if (statusDao == null) {
                    statusDao = new PostgresStatusDao(getJdbcOperations());
                }
            }
        }
        return statusDao;
    }

    public MessageDao getMessageDao() {
        if (messageDao == null) {
            synchronized (lock) {
                if (messageDao == null) {
                    messageDao = new PostgresMessageDao(getJdbcOperations());
                }
            }
        }
        return messageDao;
    }

    public ScheduleDao getScheduleDao() {
        if (scheduleDao == null) {
            synchronized (lock) {
                if (scheduleDao == null) {
                    scheduleDao = new PostgresScheduleDao(getJdbcOperations());
                }
            }
        }
        return scheduleDao;
    }

    public StatusHistoryDao getStatusHistoryDao() {
        if (statusHistoryDao == null) {
            synchronized (lock) {
                if (statusHistoryDao == null) {
                    statusHistoryDao = new PostgresStatusHistoryDao(getJdbcOperations());
                }
            }
        }
        return statusHistoryDao;
    }
}