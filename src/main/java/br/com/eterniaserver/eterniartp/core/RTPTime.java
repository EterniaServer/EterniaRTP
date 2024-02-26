package br.com.eterniaserver.eterniartp.core;

import br.com.eterniaserver.eternialib.database.annotations.DataField;
import br.com.eterniaserver.eternialib.database.annotations.PrimaryKeyField;
import br.com.eterniaserver.eternialib.database.annotations.Table;
import br.com.eterniaserver.eternialib.database.enums.FieldType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(tableName = "%eternia_rtp_table%")
public class RTPTime {

    @PrimaryKeyField(columnName = "uuid", type = FieldType.UUID, autoIncrement = false)
    private UUID uuid;

    @DataField(columnName = "last_rtp", type = FieldType.TIMESTAMP)
    private Timestamp lastRTP;

}
