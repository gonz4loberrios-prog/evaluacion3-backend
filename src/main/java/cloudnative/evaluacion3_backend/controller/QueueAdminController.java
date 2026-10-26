package cloudnative.evaluacion3_backend.controller;



import cloudnative.evaluacion3_backend.dto.QueueRequestDto;
import cloudnative.evaluacion3_backend.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/queues")
public class QueueAdminController {

    private final RabbitAdminService adminService;

    public QueueAdminController(RabbitAdminService adminService) {
        this.adminService = adminService;
    }

    // POST /api/v1/queues - Crea una cola administrada
    @PostMapping
    public ResponseEntity<String> createQueue(@Valid @RequestBody QueueRequestDto request) {
        adminService.createQueue(request.getName(), request.isDurable());
        return ResponseEntity.ok("Cola '" + request.getName() + "' creada exitosamente.");
    }

    // DELETE /api/v1/queues/{name} - Elimina una cola administrada
    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteQueue(@PathVariable("name") String name) {
        boolean deleted = adminService.deleteQueue(name);
        if (deleted) {
            return ResponseEntity.ok("Cola '" + name + "' eliminada exitosamente.");
        }
        return ResponseEntity.notFound().build();
    }
}