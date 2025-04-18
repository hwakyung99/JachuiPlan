/// 이재혁, 김성현
package com.trace.jachuiplan.building;

import com.trace.jachuiplan.building.Building;
import com.trace.jachuiplan.building.BuildingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BuildingService {

    @Autowired
    private final BuildingRepository buildingRepository;

    // 레포지토리에서 제공하는 findByCriteria 메서드를 호출
    public List<Building> findBuildingCriteria(String startYearMonth, String endYearMonth, List<String> sggcds,
                                               String rentType, Double minArea, Double maxArea, Integer minBuildYear,
                                               Integer maxBuildYear, Integer minFloor, Integer maxFloor) {
        return buildingRepository.findByCriteria(startYearMonth, endYearMonth, sggcds, rentType, minArea, maxArea, minBuildYear, maxBuildYear, minFloor, maxFloor);
    }

    public List<BuildingFilterDTO> averageBuildingCriteria(String startYearMonth, String endYearMonth, List<String> sggcds,
                                               String rentType, Double minArea, Double maxArea, Integer minBuildYear,
                                               Integer maxBuildYear, Integer minFloor, Integer maxFloor) {
        return buildingRepository.averageByCriteria(startYearMonth, endYearMonth, sggcds, rentType, minArea, maxArea, minBuildYear, maxBuildYear, minFloor, maxFloor);
    }

    // 월별 가격 추이
    public List<BuildingTransitionDTO> transitionBuildingCriteria(String sggcd, String umdnm,
                                                           String rentType, Double minArea, Double maxArea, Integer minBuildYear,
                                                           Integer maxBuildYear, Integer minFloor, Integer maxFloor) {
        return buildingRepository.averageAndCountByMonthlyAndUmd(sggcd, umdnm, rentType, minArea, maxArea, minBuildYear, maxBuildYear, minFloor, maxFloor);
    }

}
