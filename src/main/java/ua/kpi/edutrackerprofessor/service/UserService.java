package ua.kpi.edutrackerprofessor.service;

import ua.kpi.edutrackerentity.entity.User;
import ua.kpi.edutrackerprofessor.dto.ContactDataDto;

import java.util.Optional;

public interface UserService {
    boolean validPhoneByExist(ContactDataDto contactDataDto);
    boolean validEmailByExist(ContactDataDto contactDataDto);
    boolean validTelegramByExist(ContactDataDto contactDataDto);

    Optional<User> getByPhone(String phone);
    Optional<User> getByEmail(String email);
    Optional<User> getByTelegram(String telegram);
}