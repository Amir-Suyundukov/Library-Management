package com.syuyndukov.library.library_managemen.mapper;

import com.syuyndukov.library.library_managemen.domain.loan.Loan;
import com.syuyndukov.library.library_managemen.dto.loan.LoanResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanMapper {

    private final BookMapper bookMapper;
    private final UserMapper userMapper;

    public LoanMapper(BookMapper bookMapper, UserMapper userMapper) {
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
    }


    // Маппинг сущности Loan в LoanResponseDto
    // Здесь мы также рассчитываем статусы (returned, overdue, daysOverdue)
    public LoanResponseDto toDto (Loan loan){
        if(loan == null){
            return null;
        }

        LoanResponseDto dto = new LoanResponseDto();
        dto.setId(loan.getId());
        if (loan.getBook() != null){
            dto.setBook(bookMapper.toDto(loan.getBook())); //маппим книгу
        }
        if (loan.getUser() != null){
            dto.setUser(userMapper.toDto(loan.getUser())); //маппим пользователя
        }

        dto.setIssueDate(loan.getIssueDate());
        dto.setDueDate(loan.getDueDate());
        dto.setReturnDate(loan.getReturnDate());

        dto.setReturned(loan.isReturned());
        dto.setOverdue(loan.isOverdue());
        dto.setDaysOverdue(loan.getDaysOverdue());

        return dto;
    }

    // Маппинг списка сущностей Loan в список DTO LoanResponseDto
    public List<LoanResponseDto> toDtoList(List<Loan> loans){
        if (loans == null){
            return null;
        }
        // Важно: Убедись, что Book и User загружены для каждой выдачи, если они LAZY!
        // Это может потребовать @Query с JOIN FETCH в репозитории!
        return loans.stream().map(this::toDto).toList();
    }
}
