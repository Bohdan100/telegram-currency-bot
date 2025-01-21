package corp.telegrambot.users;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(User user) {
        if (!userRepository.existsByTelegramId(user.getTelegramId())) {
            userRepository.save(user);
        }
    }
}
