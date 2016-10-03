#!/usr/bin/env python
# Python utility to convert airbnb csv to sql insertion files
import csv

def main():
  with open('vancouver.csv', newline='') as f:
    reader = csv.reader(f, delimiter=' ', quotechar='|')
    i = 0
    for line in reader:
      print(', '.join(line))
      i += 1
      if i > 2: break

if __name__ == '__main__':
  main()