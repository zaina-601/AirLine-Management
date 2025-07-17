package com.alabtaal.airline.repo;

import com.alabtaal.airline.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepo extends JpaRepository<Passenger, Integer> {

}
