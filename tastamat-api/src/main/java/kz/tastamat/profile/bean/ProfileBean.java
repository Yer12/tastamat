package kz.tastamat.profile.bean;

import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.dao.ProfileDao;
import kz.tastamat.dao.UserDao;
import kz.tastamat.dao.impl.ProfileDaoImpl;
import kz.tastamat.dao.impl.UserDaoImpl;
import kz.tastamat.db.model.dto.ProfileDto;
import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.profile.dto.BalanceWithdrawDto;
import kz.tastamat.profile.dto.ProfileInfoDto;
import kz.tastamat.utils.JsonUtils;
import kz.zx.exceptions.ApiException;
import org.jooq.DSLContext;

/**
 * Created by baur on 10/24/17.
 */
public class ProfileBean {

    private DSLContext ctx;

    public ProfileBean(DSLContext ctx) {
        this.ctx = ctx;
    }

    public static ProfileBean build(DSLContext ctx) {
        return new ProfileBean(ctx);
    }

    public ProfileDto getFullInfo(Long id) {
        JsonObject profileError = JsonUtils.getDictionary("not.found.profile", "", "Профиль не найден", "", "");
        return getProfileDao(this.ctx).findById(id).orElseThrow(() -> ApiException.notFound(profileError.toString()));
    }

    public ProfileInfoDto getFullInfoByUser(Long userId) {
        return this.ctx.transactionResult(tr -> {
            JsonObject profileError = JsonUtils.getDictionary("not.found.profile", "", "Профиль не найден", "", "");
            ProfileDto profileDto = getProfileDao(this.ctx).findByUser(userId).orElseThrow(() -> ApiException.notFound(profileError.toString()));

            return ProfileInfoDto.build(profileDto);
        });
    }

    public ProfileInfoDto getFullInfoByUserPhone(String phone) {
        return this.ctx.transactionResult(tr -> {
            JsonObject userError = JsonUtils.getDictionary("phone.not.found.user", "", "Пользователь не найден", "", "");
            UserDto user = getUserDao(this.ctx).findByPhone(phone).orElseThrow(() -> ApiException.notFound(userError.encode()));

            JsonObject profileError = JsonUtils.getDictionary("not.found.profile", "", "Профиль не найден", "", "");
            ProfileDto profileDto = getProfileDao(this.ctx).findByUser(user.id).orElseThrow(() -> ApiException.notFound(profileError.toString()));

            return ProfileInfoDto.build(profileDto);
        });
    }

    public ProfileInfoDto withdrawBalance(BalanceWithdrawDto dto) {
        JsonObject profileError = JsonUtils.getDictionary("not.found.profile", "", "Профиль не найден", "", "");
        ProfileDto profileDto = getProfileDao(this.ctx).findById(dto.profileId).orElseThrow(() -> ApiException.notFound(profileError.toString()));

        JsonObject error = JsonUtils.getDictionary("not.found.auth.user", "", "Пользователь не найден", "", "");
        UserDto userDto = getUserDao(this.ctx).findById(profileDto.userId).orElseThrow(() -> ApiException.notFound(error.toString()));

        JsonObject phoneError = JsonUtils.getDictionary("not.found.auth.invalid.phone", "", "Телефон не верен", "", "");
        if (!dto.phone.equals(userDto.phone)) {
            throw ApiException.business(phoneError.toString());
        }

        JsonObject codeError = JsonUtils.getDictionary("not.found.auth.invalid.code", "", "Код не верен", "", "");
        if (!dto.code.equals(userDto.smsCode)) {
            throw ApiException.business(codeError.toString());
        }

        return this.ctx.transactionResult(tr -> {
            DSLContext dsl = tr.dsl();

            ProfileDto profileDto1 = getProfileDao(dsl).findById(dto.profileId).get();

            Long currentWalletBalance = profileDto1.wallet;
            long newWalletBalance;

            if (currentWalletBalance < dto.amount) {
                throw ApiException.business("current wallet balance less than withdrawal amount");
            } else {
                newWalletBalance = currentWalletBalance - dto.amount;
            }
            getProfileDao(dsl).wallet(dto.profileId, newWalletBalance);

            return ProfileInfoDto.build(getProfileDao(dsl).findById(dto.profileId).get());
        });
    }

    public ProfileDto template(ProfileInfoDto profile) {
        getProfileDao(this.ctx).template(profile.id, profile.template);
        return getFullInfo(profile.id);
    }

    public int wallet(Long id, Long wallet) {
        return getProfileDao(this.ctx).wallet(id, wallet);
    }

    private UserDao getUserDao(DSLContext dsl) {
        return new UserDaoImpl(dsl);
    }

    private ProfileDao getProfileDao(DSLContext dsl) {
        return new ProfileDaoImpl(dsl);
    }
}
