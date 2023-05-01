package me.volkovd.sensorsrestapi.repositories;

import me.volkovd.sensorsrestapi.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementsRepository extends JpaRepository<Measurement, Integer> {

}
