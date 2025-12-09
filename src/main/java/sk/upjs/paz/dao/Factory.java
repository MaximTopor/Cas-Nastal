package sk.upjs.paz.dao;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import sk.upjs.paz.dao.psdao.*;

public enum Factory {
    INSTANCE;

    private volatile JdbcOperations jdbcOps;

    private volatile UserDao userDao;
    private volatile RoleDao roleDao;
    private volatile DistrictDao districtDao;
    private volatile TermDao termDao;
    private volatile StatusDao statusDao;
    private volatile MessageDao messageDao;
    private volatile ScheduleDao scheduleDao;
    private volatile StatusHistoryDao statusHistoryDao;

    private final Object lock = new Object();

    public JdbcOperations getJdbcOperations() {
        if (jdbcOps == null) {
            synchronized (lock) {
                if (jdbcOps == null) {
                    var ds = new PGSimpleDataSource();
                    ds.setURL(System.getProperty("DB_JDBC", "jdbc:postgresql://localhost:5432/cn"));
                    ds.setUser(System.getProperty("DB_USER", "postgres"));
                    ds.setPassword(System.getProperty("DB_PASSWORD", "1234"));
                    jdbcOps = new JdbcTemplate(ds);
                }
            }
        }
        return jdbcOps;
    }

    public UserDao getUserDao() {
        if (userDao == null) {
            synchronized (lock) {
                if (userDao == null) {
                    userDao = new PostgresUserDao(getJdbcOperations());
                }
            }
        }
        return userDao;
    }

    public RoleDao getRoleDao() {
        if (roleDao == null) {
            synchronized (lock) {
                if (roleDao == null) {
                    roleDao = new PostgresRoleDao(getJdbcOperations());
                }
            }
        }
        return roleDao;
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