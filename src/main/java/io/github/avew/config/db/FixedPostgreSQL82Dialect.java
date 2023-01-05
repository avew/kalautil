package io.github.avew.config.db;

import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.Types;

/*
 * Developed by Asep Rojali on 2/21/19 4:15 PM
 * Last modified 2/21/19 4:15 PM
 * Copyright (c) 2019. All rights reserved.
 */

@SuppressWarnings("unused")
public class FixedPostgreSQL82Dialect extends PostgreSQL82Dialect {

	public FixedPostgreSQL82Dialect() {
		super();
		registerColumnType(Types.BLOB, "bytea");
	}

	@Override
	public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
		if (sqlTypeDescriptor.getSqlType() == Types.BLOB) {
			return BinaryTypeDescriptor.INSTANCE;
		}
		return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
	}
}