package com.littlesquare.crackerjack.services.common.auth.dao;

import com.littlesquare.crackerjack.services.common.auth.core.Person;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;
import org.skife.jdbi.v2.util.BooleanMapper;

@UseStringTemplate3StatementLocator
@RegisterMapperFactory(BeanMapperFactory.class)
public interface PersonDAO {
    @SqlQuery
    Person findByEmail(@Bind("email") String email);

    @SqlQuery
    Person findById(@Bind("id") Long personId);

    @SqlUpdate
    @GetGeneratedKeys
    long create(@BindBean Person person);

    @SqlUpdate
    void update(@Bind("password") String password, @Bind("email") String email);

    @SqlQuery
    @Mapper(BooleanMapper.class)
    Boolean doesUserHavePassword(@Bind("email") String email);
}
