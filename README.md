# VkTestTask

[![CircleCI](https://circleci.com/gh/sargeras55/VkTestTask/tree/develop.svg?style=svg)](https://circleci.com/gh/sargeras55/VkTestTask/tree/develop)

## Базовое описание архитектуры:
</br>
Ахритектура приложения сочетает принципы подходов MVI и MVVM
Данная архитектура не использует паттерн Command для реализации MVI, объекты типа Action, Effect заменены функциями, дабы избежать дополнительных аллокаций объектов.

## Базовые компоненты архитектуры:
- **class ViewProperty** - lite реализация LifeData, не требует передачи объекта LifecycleOwner, но при подписке возвращает объект Disposal, от которого View(Fragment) можнет отписаться.
- **interface marker ViewState** - имплементируется конкретной реализацией ViewState, необходим для создания контракта, содержит в себе поля типа ViewProperty
- **interface Dispatcher** - имплементируется классом ViewModel, в нем декларируются события к которым имеет доступ View(Fragment)
- **interface StateHolder** - наследуется интерфейсом Dispatcher, предоставляет доступ View(Fragment) к ViewState
- **interface StateController** - выполняет функции Reducer, наследуется от интерфейса StateHolder
- **class Store** - содержит бизнес логику приложения

## Ситуации примерения различных классов:
- Если экран не передеает никаких action и у него отсутсует State, который необходимо хранить существует реализация без ViewState и Dispatcher,
достаточно унаследовать реализацию Fragment от класса BaseFragment
- Если экран передает action, но имеет state, существует реализация для Fragment - класс FragmentWithDispatcher и для Dispatcher - интерфейс Dispatcher
- Для экранов который могу передавать эвены и имеют state необходимо использовать реализацию для Fragment - FragmentStateful, Dispatcher - DispatcherStateful
