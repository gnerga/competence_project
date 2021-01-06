How to run data generator?

1. Generate 15 users
```python
python3 data_generator.py -u 15
```
2. Generate hotspots
```python
python3 data_generator.py -hs
```
3. Generate trace
```python
python3 data_generator.py -e
```
4. Load hotspots and users from csv
```python
python3 data_generator.py -l
```
5. Set path for user.csv
```python
python3 data_generator.py -su data/user.csv
```
6. Set path for hotspots.csv
```python
python3 data_generator.py -sh data/hotspot.csv 
```
7. Set path for trace.csv
```python
python3 data_generator.py -st data/trace
```
