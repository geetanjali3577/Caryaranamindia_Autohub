package com.autohub.serviceImpl;

import com.autohub.dto.MonthlyViewDTO;
import com.autohub.dto.VehicleResponseDTO;
import com.autohub.entity.Vehicle;
import com.autohub.entity.VehicleView;
import com.autohub.exception.ResourceNotFoundException;
import com.autohub.repository.VehicleRepository;
import com.autohub.repository.VehicleViewRepository;
import com.autohub.service.VehicleViewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VehicleViewServiceImpl implements VehicleViewService {

    private final VehicleViewRepository vehicleViewRepository;
    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;

    @Override
    public void saveView(Long vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle Not Found"));

        VehicleView view = new VehicleView();

        view.setVehicle(vehicle);
        view.setDealer(vehicle.getDealer());

        vehicleViewRepository.save(view);
    }


    @Override
    public List<MonthlyViewDTO> getMonthlyViews(Long dealerId) {

        List<Object[]> result = vehicleViewRepository.getMonthlyViews(dealerId);

        String[] months = {
                "Jan","Feb","Mar","Apr","May","Jun",
                "Jul","Aug","Sep","Oct","Nov","Dec"
        };

        Map<Integer, MonthlyViewDTO> map = new HashMap<>();

        for (Object[] row : result) {

            int month = ((Number) row[0]).intValue();
            Long views = ((Number) row[1]).longValue();

            map.put(month,
                    new MonthlyViewDTO(
                            months[month - 1],
                            views
                    ));
        }

        List<MonthlyViewDTO> response = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {

            response.add(
                    map.getOrDefault(
                            i,
                            new MonthlyViewDTO(
                                    months[i - 1],
                                    0L
                            )
                    )
            );
        }

        return response;
    }

    @Override
    public VehicleResponseDTO getVehicleById(Long vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle Not Found"));

        return modelMapper.map(vehicle, VehicleResponseDTO.class);
    }
}