package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private VikingListener vikingListener;

    public VikingController(VikingService vikingService, VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
    }

    @GetMapping
    @Operation(summary = "Получить список созданных викингов", operationId = "getAllVikings")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Список успешно получен")})
    public List<Viking> getAllVikings() {
        System.out.println("GET /api/vikings called");
        return vikingService.findAll();
    }

    @GetMapping("/test")
    @Operation(summary = "Получить список тестовых викингов", operationId = "getTest")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Список успешно получен")})
    public List<String> test() {
        System.out.println("GET /api/vikings/test called");
        return List.of("Ragnar", "Bjorn");
    }

    @PostMapping("/postRandom")
    @Operation(summary = "Создать викинга со случайными параметрами", operationId = "post")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Викинг успешно создан")})
    public void addViking(){
        System.out.println("POST api/vikings/postRandom called");
        vikingListener.testAdd();
    }

    @PostMapping("/postCustom")
    @Operation(summary = "Создать викинга с заданными параметрами", operationId = "post")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Викинг успешно создан")})
    public void addCustomViking(@RequestBody Viking viking){
        System.out.println("POST api/vikings/postCustom called");
        vikingListener.addCustom(viking);
    }

    @DeleteMapping("/delete/{vikingId}")
    @Operation(summary = "Удалить викинга по id", operationId = "delete")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Викинг успешно удален")})
    public void deleteVikingById(@PathVariable("vikingId") Integer vikingId){
        System.out.println("DELETE api/vikings/delete/" + vikingId + " called");
        vikingListener.deleteVikingById(vikingId);
    }

    @PutMapping("/update")
    @Operation(summary = "Изменить викинга", operationId = "update")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Викинг успешно изменен")})
    public void updateViking(@RequestBody Viking viking){
        System.out.println("UPDATE api/vikings/update called");
        vikingListener.updateViking(viking);
    }
}