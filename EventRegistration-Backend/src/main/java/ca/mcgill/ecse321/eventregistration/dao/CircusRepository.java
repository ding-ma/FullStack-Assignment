package ca.mcgill.ecse321.eventregistration.dao;

import ca.mcgill.ecse321.eventregistration.model.Circus;
import org.springframework.data.repository.CrudRepository;

public interface CircusRepository extends CrudRepository<Circus, Integer> {
    Circus findByName(String name);
}
