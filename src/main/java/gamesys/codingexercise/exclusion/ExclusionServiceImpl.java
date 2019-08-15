package gamesys.codingexercise.exclusion;

import org.springframework.stereotype.Service;

@Service
public class ExclusionServiceImpl implements ExclusionService {
    @Override
    public boolean validate(String dob, String ssn) {
        // add a case where the validation fails
        return !"123-45-6789".equals(ssn);
    }
}
