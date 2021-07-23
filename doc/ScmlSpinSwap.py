#!/usr/bin/python
# -*- coding: UTF-8 -*-

import subprocess
import sys
import os
import re
import argparse
import xml.etree.ElementTree as ET
import xmltodict


def load_xml(path):
    tree = ET.parse(path)
    return tree


def write_xml(tree, out_path):
    tree.write(out_path, encoding="utf-8", xml_declaration=True)


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


def convert(in_path, out_folder):
    print('[INFO] Converting...: %s' % (in_path))

    fileName = getFileName(in_path)
    out_path = os.path.join(out_folder, fileName + '.scml')
    if os.path.isfile(out_path) and os.path.exists(out_path):
        out_path = os.path.join(out_folder, fileName + '_c.scml')

    toXml = load_xml('%s%s' % (in_path, '.scml'))
    fromXml = load_xml('%s_000%s' % (in_path, '.scml'))

    toXml_entities = toXml.getroot().findall('entity')
    fromXml_entities = fromXml.getroot().findall('entity')

    fromAnims = {}
    fromObjs = {}
    for e in fromXml_entities:
        xml_anis = e.findall('animation')
        for anim in xml_anis:
            # xml_mainline = anim.find('mainline')
            # xml_keys = xml_mainline.findall('key')
            # m_keys = {}
            # for key in xml_keys:
            #     objs = key.findall('object_ref')
            #     m_keys[key.get('id')] = int(len(objs)) if objs else 0
            # fromObjs[anim.get('name')] = m_keys

            xml_timelines = anim.findall('timeline')
            times = {}
            for timeline in xml_timelines:
                xml_keys = timeline.findall('key')
                keys = {}
                for key in xml_keys:
                    keys['%s' % (key.get('id'))] = key.get('spin', '0')
                times[timeline.get('name')] = keys
            fromAnims[anim.get('name')] = times

    for e in toXml_entities:
        xml_anis = e.findall('animation')
        for anim in xml_anis:
            # xml_mainline = anim.find('mainline')
            # xml_keys = xml_mainline.findall('key')
            # m_keys = fromObjs[anim.get('name')]
            # for key in xml_keys:
            #     size = m_keys[key.get('id')]
            #     objs = key.findall('object_ref')
            #     i = 0
            #     for obj in objs:
            #         size -= 1
            #         obj.set('timeline', '%s' % size)
            #         obj.set('z_index', '%s' % i)
            #         i += 1

            _anim = fromAnims[anim.get('name')]
            xml_timelines = anim.findall('timeline')
            for timeline in xml_timelines:
                time = _anim[timeline.get('name')]
                xml_keys = timeline.findall('key')
                for key in xml_keys:
                    spin = time[key.get('id')]
                    key.set('spin', spin)

    write_xml(toXml, out_path)


def main(in_path, out_path):
    if not os.path.exists(in_path):
        print('[ERROR] \'input path\': %s unexist!' % (in_path))
        return

    print('[INFO] Start convert: %s => %s' % (in_path, out_path))

    if not os.path.isdir(in_path):
        file_name = None
        if os.path.isdir(out_path) or out_path.endswith('/') or out_path.endswith('\\'):
            if not os.path.exists(out_path):
                makedir_p(out_path)

            file_name = os.path.splitext(os.path.basename(in_path))[0]
        else:
            file_name = os.path.splitext(os.path.basename(out_path))[0]
            out_path = os.path.dirname(out_path)

        convert(in_path.replace(".scml", ''), out_path)

    for root, dirs, files in os.walk(in_path):
        out_folder = os.path.join(out_path, re.sub(r'^[\\/]', '', root.replace(in_path, '')))

        if not os.path.exists(out_folder):
            makedir_p(out_folder)

        for f in files:
            if not f.lower().endswith('.scml') or f.lower().endswith('_000.scml'): continue
            in_file = os.path.join(root, f)
            out_file = os.path.splitext(f)[0]
            convert(in_file.replace(".scml", ''), out_folder)

    print('[INFO] End convert!')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='A simple converter that use for spriter file convert to spine file.')
    parser.add_argument('-i', type=str, dest='in_path',
                        help='Directory or File to be converted. By default, point to the current directory.')
    parser.add_argument('-o', type=str, dest='out_path',
                        help='Directory or File convert to. By default, point to the current directory.')
    args = parser.parse_args()

    main(args.in_path or './', args.out_path or './')
