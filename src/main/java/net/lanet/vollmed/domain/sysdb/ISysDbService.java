package net.lanet.vollmed.domain.sysdb;

import java.time.LocalDateTime;

public interface ISysDbService {
    LocalDateTime findDateFirstTableCreated(String databaseName);
    LocalDateTime findDateTable(String tableName);
}
