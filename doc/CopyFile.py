#!/usr/bin/python
# -*- coding: UTF-8 -*-

import sys
import os
import re
import argparse
import shutil


def makedir_p(path):
    try:
        os.makedirs(path)
    except OSError as exc:
        if exc.errno != errno.EEXIST:
            raise


def getFileName(in_path):
    names = []
    if str.find(r'/', in_path) >= 0:
        names = str.split(in_path, '/')
    else:
        names = str.split(in_path, '\\')
    if len(names) > 0:
        return names[len(names) - 1]
    return in_path


def main(in_path, out_path):
    if not os.path.exists(in_path):
        print('[ERROR] \'input path\': %s unexist!' % (in_path))
        return

    print('[INFO] Start: %s => %s' % (in_path, out_path))

    if not os.path.isdir(in_path):
        if os.path.isdir(out_path) or out_path.endswith('/') or out_path.endswith('\\'):
            if not os.path.exists(out_path):
                makedir_p(out_path)
        if not in_path.endswith('.scml'):
            return
        shutil.copyfile(in_path, os.path.join(out_path, getFileName(in_path)))

    if not os.path.exists(out_path):
        makedir_p(out_path)

    for root, dirs, files in os.walk(in_path):
        for f in files:
            if not f.lower().endswith('.scml'): continue
            in_file = os.path.join(root, f)
            shutil.copyfile(in_file, os.path.join(out_path, getFileName(in_file)))

    print('[INFO] End!')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='A simple converter that use for spriter file convert to spine file.')
    parser.add_argument('-i', type=str, dest='in_path',
                        help='Directory or File to be converted. By default, point to the current directory.')
    parser.add_argument('-o', type=str, dest='out_path',
                        help='Directory or File convert to. By default, point to the current directory.')
    args = parser.parse_args()

    main(args.in_path or './', args.out_path or './')
