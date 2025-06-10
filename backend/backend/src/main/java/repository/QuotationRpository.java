package repository;

import entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRpository extends JpaRepository<Quotation, Long> {
}
