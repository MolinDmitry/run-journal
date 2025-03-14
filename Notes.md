# Заметки


# Заметки


## Типы тренировок (ActivityType):
- Бег
- Хайкинг
- Треккинг
- Прогулка
- Велопоход
- Велопрогулка
- Велогонка
- Лыжная прогулка
- Беговые лыжи. Классический стиль.
- Беговые лыжи. Свободный стиль.


## Информация о тренировке:
- Первичные данные:
    - Тип тренировки
    - Комментарий
    - Трек (содержит набор точек, в каждой информация о времени, широте, долготе, скорости, пульсе, каденсе)
- Вторичные данные
    - Длительность
    - Дистанция
    - Дата и время начала

## Работа с Mockito
- Добавляем зависимости
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <version>5.16.0</version>
        <scope>test</scope>
	</dependency>
    <dependency>
			<groupId>org.mockito</groupId>
			<artifactId>mockito-core</artifactId>
			<version>5.16.0</version>
			<scope>test</scope>
	</dependency>
- В классе тестов добавлем аннотацию   `@ExtendWith(MockitoExtension.class)` для инициализации
- Создаем макеты объектов. 
    Например 
                TrackPoints mockTrackPoint1 = Mockito.mock(TrackPoints.class) 
    или просто 
                TrackPoints mockTrackPoint2 = mock();
- Также макет можно создать в виде поля класса:
                @Mock
                TrackPoints mockTrackPoint1
    Тогда метод mock() можно не использовать
- Добавляем заглушки (stub) методов. Например:
                Mockito.when(mockTrackPoint1.getLatitude()).thenReturn(55.75);



  



