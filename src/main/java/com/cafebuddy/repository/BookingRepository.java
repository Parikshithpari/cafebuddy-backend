package com.cafebuddy.repository;

import com.cafebuddy.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByStartTimeDesc(Long userId);
    List<Booking> findByCafeIdOrderByStartTimeDesc(Long cafeId);
    Optional<Booking> findByUserIdAndEndTimeAfter(Long userId, Instant now);
    
    @Query("""
    	       SELECT b FROM Booking b
    	       WHERE b.endTime BETWEEN :now AND :future
    	       AND b.status = 'CONFIRMED'
    	       """)
    	List<Booking> findBookingsEndingBetween(
    	        @Param("now") Instant now,
    	        @Param("future") Instant future
    	);
    
    @Query("""
    	       SELECT COALESCE(SUM(b.people), 0)
    	       FROM Booking b
    	       WHERE b.cafe.id = :cafeId
    	       AND b.startTime <= :now
    	       AND b.endTime >= :now
    	       """)
    	Integer countActivePeople(
    	        @Param("cafeId") Long cafeId,
    	        @Param("now") Instant now
    	);
    
    @Query("""
    	       SELECT b FROM Booking b
    	       WHERE b.endTime < :now
    	       AND b.status <> 'COMPLETED'
    	       """)
    	List<Booking> findExpiredBookings(
    	        @Param("now") Instant now
    	);
    
    @Query("""
    		SELECT b FROM Booking b
    		JOIN FETCH b.user
    		JOIN FETCH b.cafe
    		WHERE b.endTime BETWEEN :now AND :future
    		AND b.status = 'CONFIRMED'
    		AND b.reminderSent = false
    		""")
    		List<Booking> findBookingsEndingSoon(
    		        @Param("now") Instant now,
    		        @Param("future") Instant future
    		);
}
