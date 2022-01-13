# interview_crew_raketa
Реализовал следующие методы:

@PostMapping public ResponseEntity createValues(@RequestParam @Min(0) Integer number) по данному методу хотелось бы ложить пачками, а не как это делает hibernate- по 1 записи;
я так и не понял, почему не проходит валидация, буду рад, если Вы подскажите, по всем канонам должно валидироваться-но нет, поэтому проверяю ручками что не отрицательное число пришло на вход

@DeleteMapping public ResponseEntity removeAll() { удаляю данные через truncate - так лучше производительность

@PutMapping("/{id}") public ResponseEntity updateValue(@PathVariable long id, @Valid @RequestBody ValueDto valueDto) {

@GetMapping public ResponseEntity<List> findAll(@RequestParam(required = false) Map<String, String> allRequestParams) { т.к в задании реализовать выгрузку ВСЕЙ таблицы, решил делать без пагинации in работает только с целыми числами,как в тз

Обработка @ExceptionHandler была опущена, так как не требовалась по тз.
