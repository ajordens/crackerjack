package com.littlesquare.crackerjack.services.common.auth.dao

import com.littlesquare.crackerjack.services.common.auth.core.MultiFactorAuth
import com.littlesquare.crackerjack.services.common.auth.core.Oauth
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys
import org.skife.jdbi.v2.sqlobject.BindBean

import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory
import org.skife.jdbi.v2.tweak.BeanMapperFactory
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.Bind

/**
 * @author Adam Jordens (adam@jordens.org)
 */
@UseStringTemplate3StatementLocator
@RegisterMapperFactory(BeanMapperFactory.class)
public interface AuthDAO {
    @SqlUpdate
    @GetGeneratedKeys
    long createOauth(@BindBean Oauth oauth)

    @SqlUpdate
    @GetGeneratedKeys
    long createMultiFactorAuth(@BindBean MultiFactorAuth multiFactorAuth)

    @SqlQuery
    Oauth findOauthByPersonId(@Bind("person_id") Long personId)

    @SqlQuery
    MultiFactorAuth findMultiFactorAuthByPersonId(@Bind("person_id") Long personId, @Bind("type") String type)
}
