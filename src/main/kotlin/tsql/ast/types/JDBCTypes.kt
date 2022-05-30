package tsql.ast.types

enum class JDBCTypes(name: String) {
    CHAR("CHAR") {
        override fun toEType(): EType {
            return EType.STRING
        }
    },
    VARCHAR("VARCHAR") {
        override fun toEType(): EType {
            return EType.STRING
        }
    },
    NUMERIC("NUMERIC") {
        override fun toEType(): EType {
            TODO("Not yet implemented")
        }
    },
    DECIMAL("decimal") {
        override fun toEType(): EType {
            return EType.DECIMAL
        }
    },
    BIT("BIT") {
        override fun toEType(): EType {
            return EType.BOOL
        }
    },
    TINYINT("TINYINT") {
        override fun toEType(): EType {
            return EType.INT
        }
    },
    SMALLINT("SMALLINT") {
        override fun toEType(): EType {
            return EType.INT
        }
    },
    INTEGER("INTEGER") {
        override fun toEType(): EType {
            return EType.INT
        }
    },
    BIGINT("BIGINT") {
        override fun toEType(): EType {
            return EType.BIGINT
        }
    },
    FLOAT("FLOAT") {
        override fun toEType(): EType {
            return EType.FLOAT
        }
    },
    DOUBLE("DOUBLE") {
        override fun toEType(): EType {
            return EType.DOUBLE
        }
    },
    BINARY("BINARY") {
        override fun toEType(): EType {
            return EType.UNKNOWN
        }
    },
    REAL("REAL") {
        override fun toEType(): EType {
            return EType.FLOAT
        }
    },
    NULL("NULL") {
        override fun toEType(): EType {
            return EType.NULL
        }
    },
    VARBINARY("VARBINARY") {
        override fun toEType(): EType {
            return EType.UNKNOWN
        }
    },
    LONGVARBINARY("LONGVARBINARY") {
        override fun toEType(): EType {
            return EType.UNKNOWN
        }
    },
    DATE("DATE") {
        override fun toEType(): EType {
            return EType.DATE
        }
    },
    TIME("TIME") {
        override fun toEType(): EType {
            return EType.DATETIME
        }
    },
    TIMESTAMP("TIMESTAMP") {
        override fun toEType(): EType {
            return EType.TIMESTAMP
        }
    },
    CLOB("CLOB") {
        override fun toEType(): EType {
            return EType.UNKNOWN
        }
    },
    BLOB("BLOB") {
        override fun toEType(): EType {
            return EType.BLOB
        }
    },
    ARRAY("ARRAY") {
        override fun toEType(): EType {
            return EType.UNKNOWN
        }
    },
    STRUCT("STRUCT") {
        override fun toEType(): EType {
            return EType.UNKNOWN
        }
    },
    REF("REF") {
        override fun toEType(): EType {
            return EType.UNKNOWN
        }
    },
    JAVA_OBJECT("JAVA_OBJECT") {
        override fun toEType(): EType {
            return EType.UNKNOWN
        }
    };

    abstract fun toEType(): EType
}
