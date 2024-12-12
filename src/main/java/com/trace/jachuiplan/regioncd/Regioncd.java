package com.trace.jachuiplan.regioncd;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Regioncd")
public class Regioncd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SIDO_CD", length = 2, nullable = true)
    private String sidoCd;

    @Column(name = "SGG_CD", length = 3, nullable = true)
    private String sggCd;

    @Column(name = "LOCATADD_NM", length = 100, nullable = true)
    private String locataddNm;
}
