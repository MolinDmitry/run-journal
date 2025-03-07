package ru.project.runjournal.run_journal;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @brief Строка детальной информации сведений за км 
 */

@Data@AllArgsConstructor
public class DistanceDetailsRow {
    private short milestone;
    private String pace;
    private short hr;
    private String timeString;
}
