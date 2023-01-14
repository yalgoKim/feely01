package com.project.feeely.handler;

import com.project.feeely.dto.enums.Roles;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleEnumHandler<E extends Enum<E>> implements TypeHandler<Roles> {

    private Class<E> type;

    public RoleEnumHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, Roles parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i , parameter.getCode());
    }

    @Override
    public Roles getResult(ResultSet rs, String columnName) throws SQLException {
        return getCode(rs.getString(columnName));
    }

    @Override
    public Roles getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getCode(rs.getString(columnIndex));
    }

    @Override
    public Roles getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getCode(cs.getString(columnIndex));
    }


    private Roles getCode(String code) {
        try {
            Roles[] enumConstants = (Roles[]) type.getEnumConstants();
            for (Roles enumElement : enumConstants) {
                if (enumElement.getCode().equals(code)) {
                    return enumElement;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}
