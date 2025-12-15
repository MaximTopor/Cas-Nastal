package sk.upjs.paz.dao;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import sk.upjs.paz.dao.psdao.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public enum Factory {
    INSTANCE;

    private volatile JdbcOperations jdbc;

    private volatile UserDao UserDao;
    private volatile RoleDao roleDao;
    private volatile DistrictDao districtDao;
    private volatile TermDao termDao;
    private volatile StatusDao statusDao;
    private volatile MessageDao messageDao;
    private volatile ScheduleDao scheduleDao;
    private volatile StatusHistoryDao statusHistoryDao;

    private final Object lock = new Object();

    Factory() {
        this.jdbc = createJdbc();
        runSql("init/init.sql");
        runSql("init/test.sql");
    }

    private JdbcOperations createJdbc() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5433/cn");
        ds.setUsername("casnast");
        ds.setPassword("casnast");

        return new JdbcTemplate(ds);
    }

    private void runSql(String path) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) return;

            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            jdbc.execute(sql);

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute " + path, e);
        }
    }

    public JdbcOperations getJdbcOperations() {
        if (jdbc == null) {
            synchronized (lock) {
                if (jdbc == null) {
                    var ds = new PGSimpleDataSource();
                    ds.setURL(System.getProperty("DB_JDBC", "jdbc:postgresql://localhost:5432/cn"));
                    ds.setUser(System.getProperty("DB_USER", "postgres"));
                    ds.setPassword(System.getProperty("DB_PASSWORD", "1234"));
                    jdbc = new JdbcTemplate(ds);
                }
            }
        }
        return jdbc;
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