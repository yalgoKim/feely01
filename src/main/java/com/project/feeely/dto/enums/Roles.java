package com.project.feeely.dto.enums;

import com.project.feeely.handler.RoleEnumHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

import java.util.Arrays;
import java.util.function.Function;


@Getter
public enum Roles implements CodeEnum {
    // USER;
    NO_GRANT("USER_001" , "ROLE_USER"  , new Options[]{}, price -> price),
    BRONZE("USER_002"  , "ROLE_USER" , new Options[]{Options.NO_ADVERTISEMENT}
            , price ->  (price - Math.round(price.doubleValue() * 0.02))),
    SILVER("USER_003"  , "ROLE_USER" , new Options[]{Options.NO_ADVERTISEMENT},
            price ->  (price - Math.round(price.doubleValue() * 0.05)) ),
    GOLD("USER_004"  , "ROLE_USER" , new Options[]{Options.NO_ADVERTISEMENT},
            price ->  (price - Math.round(price.doubleValue() * 0.1)));

    private final String code;  // -> ROLE  USER , ROLE_TEST_USER
    private final String role;

    private final Options[] options;
    private final Function<Long , Long> discount;

    Roles(String code, String role, Options[] options, Function<Long, Long> discount) {
        this.code = code;
        this.role = role;
        this.options = options;
        this.discount = discount;
    }

    public static Long findByCodeGetDiscount (String code, Long price) {
        return Arrays.stream(Roles.values())
                .filter(x->x.code.equals(code))
                .findAny()
                .map(find -> find.getDiscountFunction(price))
                .orElse(Roles.NO_GRANT.getDiscountFunction(price));
    }

    private Long getDiscountFunction(Long price) {
        return this.discount.apply(price);
    }

    public static Roles findByCodeEnum(String code) {
        return Arrays.stream(Roles.values())
                .filter(x -> x.code.equals(code))
                .findAny()
                .orElse(Roles.NO_GRANT);
    }

    @MappedTypes(Roles.class)
    public static class TypeHandler extends RoleEnumHandler<Roles> {
        public TypeHandler(){
            super(Roles.class);
        }
    }

}
