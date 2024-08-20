package ua.kpi.edutrackerprofessor.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kpi.edutrackerentity.entity.User;
import ua.kpi.edutrackerprofessor.dto.ContactDataDto;
import ua.kpi.edutrackerprofessor.repository.UserRepository;
import ua.kpi.edutrackerprofessor.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public boolean validPhoneByExist(ContactDataDto contactDataDto) {
        Optional<User> user = getByPhone(contactDataDto.getPhone());
        return user.isEmpty() || user.get().getId().equals(contactDataDto.getId());
    }
    @Override
    public boolean validEmailByExist(ContactDataDto contactDataDto) {
        Optional<User> user = getByEmail(contactDataDto.getPhone());
        return user.isEmpty() || user.get().getId().equals(contactDataDto.getId());
    }
    @Override
    public boolean validTelegramByExist(ContactDataDto contactDataDto) {
        Optional<User> user = getByTelegram(contactDataDto.getTelegram());
        return user.isEmpty() || user.get().getId().equals(contactDataDto.getId());
    }

    @Override
    public Optional<User> getByPhone(String phone) {
        return userRepository.findFirstByPhone(phone);
    }
    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }
    @Override
    public Optional<User> getByTelegram(String telegram) {
        return userRepository.findFirstByTelegram(telegram);
    }
}