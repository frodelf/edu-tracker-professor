package ua.kpi.edutrackerprofessor.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackerentity.entity.User;
import ua.kpi.edutrackerprofessor.dto.ContactDataDto;
import ua.kpi.edutrackerprofessor.repository.UserRepository;
import ua.kpi.edutrackerprofessor.service.UserService;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public boolean validPhoneByExist(ContactDataDto contactDataDto) {
        log.info("UserServiceImpl validPhoneByExist start");
        Optional<User> user = getByPhone(contactDataDto.getPhone());
        boolean result = user.isEmpty() || user.get().getId().equals(contactDataDto.getId());
        log.info("UserServiceImpl validPhoneByExist finish");
        return result;
    }
    @Override
    public boolean validEmailByExist(ContactDataDto contactDataDto) {
        log.info("UserServiceImpl validEmailByExist start");
        Optional<User> user = getByEmail(contactDataDto.getPhone());
        boolean result =  user.isEmpty() || user.get().getId().equals(contactDataDto.getId());
        log.info("UserServiceImpl validEmailByExist finish");
        return result;
    }
    @Override
    public boolean validTelegramByExist(ContactDataDto contactDataDto) {
        log.info("UserServiceImpl validTelegramByExist start");
        Optional<User> user = getByTelegram(contactDataDto.getTelegram());
        boolean result =  user.isEmpty() || user.get().getId().equals(contactDataDto.getId());
        log.info("UserServiceImpl validTelegramByExist finish");
        return result;
    }

    @Override
    public Optional<User> getByPhone(String phone) {
        log.info("UserServiceImpl getByPhone start");
        Optional<User> user = userRepository.findFirstByPhone(phone);
        log.info("UserServiceImpl getByPhone finish");
        return user;
    }
    @Override
    public Optional<User> getByEmail(String email) {
        log.info("UserServiceImpl getByEmail start");
        Optional<User> user =  userRepository.findFirstByEmail(email);
        log.info("UserServiceImpl getByEmail finish");
        return user;
    }
    @Override
    public Optional<User> getByTelegram(String telegram) {
        log.info("UserServiceImpl getByTelegram start");
        Optional<User> user =  userRepository.findFirstByTelegram(telegram);
        log.info("UserServiceImpl getByTelegram finish");
        return user;
    }
}